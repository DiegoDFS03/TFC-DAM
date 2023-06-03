import json
import secrets

import bcrypt
from django.db.models import Q
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import Person, Offer, Store, UserOffer


def log(request):
    if request.method != 'GET':
        return JsonResponse({"error": "Método http no soportado"})

    sessionToken = request.headers.get('Token')
    try:
        user = Person.objects.get(token=sessionToken)
    except Person.DoesNotExist:
        return JsonResponse({"error": "Token inválido"}, status=404)

    return JsonResponse({"status": "ok"}, status=200)


@csrf_exempt
def register(request):
    if request.method != "POST":
        return JsonResponse({'error': 'HTTP method unsupported'}, status=405)

    body_json = json.loads(request.body)

    try:
        json_username = body_json['name']
        json_surname = body_json['surnames']
        json_email = body_json['email']
        json_password = body_json['password']


    except KeyError:
        return JsonResponse({'error': 'Missing paramenter in JSON'}, status=400)

    try:
        alreadyregister = Person.objects.get(email=json_email)
        return JsonResponse({"error": "Email already registered"}, status=409)
    except Person.DoesNotExist:
        salted_and_hashed_pass = bcrypt.hashpw(json_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
        user_object = Person(email=json_email,
                             password=salted_and_hashed_pass,
                             name=json_username,
                             surnames=json_surname,
                             token=None,
                             password_token=None
                             )
        user_object.save()
        return JsonResponse({"is_created": True}, status=201)


@csrf_exempt
def sessions(request):
    if request.method != 'POST':
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    body_json = json.loads(request.body)
    try:
        json_email = body_json['email']
        json_password = body_json['password']
    except KeyError:
        return JsonResponse({"error": "Faltán parámetros"}, status=400)

    try:
        db_user = Person.objects.get(email=json_email)
    except Person.DoesNotExist:
        return JsonResponse({"error": "Usuario no encontrado"}, status=404)

    if bcrypt.checkpw(json_password.encode('utf8'), db_user.password.encode('utf8')):
        random_token = secrets.token_hex(10)
        db_user.token = random_token
        db_user.save()
        return JsonResponse({"sessionToken": random_token}, status=201)
    else:
        return JsonResponse({"error": "Contraseña incorrecta"}, status=401)


def offers(request):
    if request.method != 'GET':
        return JsonResponse({"error": "Método HTTP no soportado"}, status=405)

    # Cantidad de resultados por página
    size = request.GET.get("size", None)

    # Posicion del primera oferta a mostrar en la pagina
    offset = request.GET.get("offset", None)

    # Nombre de la oferta
    title = request.GET.get("title", None)

    if size is None:
        if offset is None:
            ofertas = Offer.objects.filter(Q(title__startswith=title)).values_list('title',
                                                                                 'store__name',
                                                                                 'image',
                                                                                 'discount_percentage',
                                                                                 'original_price',
                                                                                 'end_date')
        else:
            try:
                offset = int(offset)
            except ValueError:
                return JsonResponse({"error": "Parámetro offset erróneo"}, status=400)

            return JsonResponse({"error": "Faltán parámetros"}, status=400)
    else:
        try:
            size = int(size)
        except ValueError:
            return JsonResponse({"error": "Parámetro size erróneo"}, status=400)

        if offset is None:
            return JsonResponse({"error": "Faltán parámetros"}, status=400)
        else:
            try:
                offset = int(offset)
            except ValueError:
                return JsonResponse({"error": "Parámetro offset erróneo"}, status=400)

            if title is None or len(title) == 0:
                ofertas = Offer.objects.all().values_list('title',
                                                          'store__name',
                                                          'image',
                                                          'discount_percentage',
                                                          'original_price',
                                                          'end_date')[offset:offset + size]
            else:
                ofertas = Offer.objects.filter(Q(title__startswith=title)).values_list('title',
                                                                                     'store__name',
                                                                                     'image',
                                                                                     'discount_percentage',
                                                                                     'original_price',
                                                                                     'end_date')[offset:offset + size]

    count = Offer.objects.count()

    results = []
    if ofertas is not None:
        for offer in ofertas:
            results.append({"title": offer[0],
                            "store__name": offer[1],
                            "image": offer[2],
                            "discount_percentage": offer[3],
                            "original_price": offer[4],
                            "end_date": offer[5]})

    return JsonResponse({"count": count, "results": results}, safe=False)
