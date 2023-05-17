from django.db import models


class User(models.Model):
    name = models.CharField(max_length=50)
    surnames = models.CharField(max_length=100)
    email = models.EmailField()
    password = models.CharField(max_length=150)
    token = models.CharField(max_length=20, unique=True, null=True)
    password_token = models.CharField(max_length=20, null=True)


class Store(models.Model):
    name = models.CharField(max_length=40)
    logo = models.CharField(max_length=50)


class Offer(models.Model):
    title = models.CharField(max_length=100)
    description = models.TextField()
    image = models.CharField(max_length=50)
    original_price = models.DecimalField(max_digits=8, decimal_places=2)
    genre = models.CharField(max_length=50)
    release_date = models.DateField()
    developer = models.CharField(max_length=70)
    publisher = models.CharField(max_length=70)
    store = models.ForeignKey(Store, on_delete=models.CASCADE)
    discount_percentage = models.DecimalField(max_digits=5,decimal_places=0)
    start_date = models.DateField()
    end_date = models.DateField()


class UserOffer(models.Model):
    offer = models.ForeignKey(Offer, on_delete=models.CASCADE)
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    date = models.DateField(auto_now=True)
