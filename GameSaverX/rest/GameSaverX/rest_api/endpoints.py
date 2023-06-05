import json
import secrets

import bcrypt
from django.db.models import Q
from django.http import JsonResponse
from django.utils.timezone import now
from django.views.decorators.csrf import csrf_exempt


from .models import Person, Offer, UserOffer


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
            ofertas = Offer.objects.filter(Q(title__startswith=title)).values_list('id',
                                                                                   'title',
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
                ofertas = Offer.objects.all().values_list('id',
                                                          'title',
                                                          'store__name',
                                                          'image',
                                                          'discount_percentage',
                                                          'original_price',
                                                          'end_date',
                                                          )[offset:offset + size]
            else:
                ofertas = Offer.objects.filter(Q(title__startswith=title)).values_list('id',
                                                                                       'title',
                                                                                       'store__name',
                                                                                       'image',
                                                                                       'discount_percentage',
                                                                                       'original_price',
                                                                                       'end_date',
                                                                                       )[offset:offset + size]

    count = Offer.objects.count()

    results = []
    if ofertas is not None:
        for offer in ofertas:
            results.append({"id": offer[0],
                            "title": offer[1],
                            "store__name": offer[2],
                            "image": offer[3],
                            "discount_percentage": offer[4],
                            "original_price": offer[5],
                            "end_date": offer[6]})

    return JsonResponse({"count": count, "results": results}, safe=False)


def offer(request, id_game):
    if request.method != "GET":
        return JsonResponse({"error": "HTTP method not supported"}, status=405)

    try:
        offer = Offer.objects.get(id=id_game)
    except Offer.DoesNotExist:
        return JsonResponse({"error": "No existe"}, status=404)

    return JsonResponse({"id": id_game, "title": offer.title, "description": offer.description, "image": offer.image,
                         "original_price": offer.original_price, "genre": offer.genre,
                         "url": offer.url,
                         "release_date": offer.release_date, "developer": offer.developer, "publisher": offer.publisher,
                         "discount_percentage": offer.discount_percentage, "end_date": offer.end_date})


@csrf_exempt
def saved(request, id_game):
    try:
        v = Offer.objects.get(id=id_game)
    except Offer.DoesNotExist:
        return JsonResponse({"error": "No existe"}, status=404)

    token_cabeceras = request.headers.get("Token")
    if token_cabeceras is None:
        return JsonResponse({"error": "Falta token en la cabecera"}, status=401)
    else:
        try:
            u = Person.objects.get(token=token_cabeceras)
        except Person.DoesNotExist:
            return JsonResponse({"error": "Usuario no logeado"}, status=401)

    if request.method == "PUT":
        try:
            user_offer = UserOffer.objects.get(offer=v, person=u)
            return JsonResponse({"status": "Todo OK"}, status=200)
        except UserOffer.DoesNotExist:
            new_user_offer = UserOffer(offer=v, person=u)
            new_user_offer.save()
            return JsonResponse({"status": "Todo OK"}, status=200)

    elif request.method == "DELETE":
        try:
            user_offer = UserOffer.objects.get(offer=v, person=u)
            user_offer.delete()
            return JsonResponse({"status": "Todo OK"}, status=200)
        except UserOffer.DoesNotExist:
            return JsonResponse({"status": "Todo OK"}, status=200)

    elif request.method == "GET":
        try:
            user_offer = UserOffer.objects.get(offer=v, person=u)
            return JsonResponse({"status": "Todo OK"}, status=200)
        except UserOffer.DoesNotExist:
            return JsonResponse({"error": "No existe en guardados"}, status=404)


@csrf_exempt
def favourites(request):
    token_cabeceras = request.headers.get("Token")
    if token_cabeceras is None:
        return JsonResponse({"error": "Falta token en la cabecera"}, status=401)
    else:
        try:
            u = Person.objects.get(token=token_cabeceras)
        except Person.DoesNotExist:
            return JsonResponse({"error": "Usuario no logeado"}, status=401)

    if request.method == "GET":
        favourites = []
        offers = UserOffer.objects.filter(person__token=token_cabeceras)
        for offer in offers:
            favourites.append({"id": offer.offer.id,
                               "title": offer.offer.title,
                               "store__name": offer.offer.store.name,
                               "image": offer.offer.image,
                               "discount_percentage": offer.offer.discount_percentage,
                               "original_price": offer.offer.original_price,
                               "end_date": offer.offer.end_date})
        return JsonResponse({"results": favourites}, safe=False)


@csrf_exempt
def profile(request):
    token_cabeceras = request.headers.get("Token")
    if token_cabeceras is None:
        return JsonResponse({"error": "Falta token en la cabecera"}, status=401)
    else:
        try:
            u = Person.objects.get(token=token_cabeceras)
        except Person.DoesNotExist:
            return JsonResponse({"error": "Usuario no logeado"}, status=401)
    if request.method == "GET":
        json_response = {
            "name": u.name,
            "surnames": u.surnames,
            "email": u.email,
        }
        return JsonResponse(json_response, status=200)

    if request.method == "PUT":
        body_json = json.loads(request.body)

        u.name = body_json["name"]
        u.surnames = body_json["surnames"]
        u.save()
        return JsonResponse({"status": "Todo OK"}, status=200)


@csrf_exempt
def password(request):
    if request.method != 'POST':
        return JsonResponse({"error": "Método http no soportado"})

    token_cabeceras = request.headers.get("Token")
    if token_cabeceras is None:

        body_json = json.loads(request.body)
        try:
            json_token = body_json['passwordToken']
            json_password = body_json['newPassword']
        except KeyError:
            return JsonResponse({"error": "Faltán parámetros"}, status=400)
        try:
            user = Person.objects.get(password_token=json_token)
        except Person.DoesNotExist:
            return JsonResponse({"error": "Token inválido"}, status=404)

        salted_and_hashed_pass = bcrypt.hashpw(json_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
        user.password_token = None
        user.password = salted_and_hashed_pass
        user.save()

        return JsonResponse({"Mensaje": "Contraseña cambiada"}, status=201)

    else:
        try:

            body_json = json.loads(request.body)

            try:
                json_password = body_json['oldPassword']
                new_json_password = body_json['newPassword']

            except KeyError:
                return JsonResponse({"error": "Faltán parámetros"}, status=400)

            u = Person.objects.get(token=token_cabeceras)

            if bcrypt.checkpw(json_password.encode('utf8'), u.password.encode('utf8')):
                salted_and_hashed_pass = bcrypt.hashpw(new_json_password.encode('utf8'), bcrypt.gensalt()).decode(
                    'utf8')
                u.password = salted_and_hashed_pass
                u.save()

                return JsonResponse({"Mensaje": "Contraseña cambiada"}, status=201)
            else:
                return JsonResponse({"error": "Contraseña no válida"}, status=404)



        except Person.DoesNotExist:
            return JsonResponse({"error": "Usuario no logeado"}, status=401)


def delete_offers(request):
    if request.method != 'DELETE':
        return JsonResponse({"error": "Método http no soportado"})
    # Obtén la fecha actual
    fecha_actual = now().date()

    # Filtra las ofertas cuya fecha de finalización ha pasado
    ofertas_vencidas = Offer.objects.filter(end_date__lt=fecha_actual)

    # Elimina las ofertas vencidas
    ofertas_vencidas.delete()



