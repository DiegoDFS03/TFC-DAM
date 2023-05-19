from django.contrib import admin
from .models import Person,UserOffer,Store,Offer
# Register your models here.

admin.site.register(Person)
admin.site.register(Offer)
admin.site.register(Store)
admin.site.register(UserOffer)