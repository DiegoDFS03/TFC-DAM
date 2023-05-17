import json
import bcrypt
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import Person,Offer,Store,UserOffer


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