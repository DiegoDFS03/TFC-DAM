"""
URL configuration for GameSaverX project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path

from rest_api import endpoints

urlpatterns = [
    path('admin/', admin.site.urls),
    path('v1/log', endpoints.log),
    path('v1/users', endpoints.register),
    path('v1/sessions', endpoints.sessions),
    path('v1/offers', endpoints.offers),
    path('v1/offer/<int:id_game>', endpoints.offer),
    path('v1/offer/<int:id_game>/saved', endpoints.saved),
    path('v1/saved', endpoints.favourites),
    path('v1/profile', endpoints.profile),
    path('v1/password', endpoints.password),
    path('v1/delete_expired', endpoints.delete_offers)

]
