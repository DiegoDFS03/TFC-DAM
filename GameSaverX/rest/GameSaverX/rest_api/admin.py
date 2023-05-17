from django.contrib import admin
from .models import User,UserOffer,Store,Offer
# Register your models here.

admin.site.register(User)
admin.site.register(Offer)
admin.site.register(Store)
admin.site.register(UserOffer)