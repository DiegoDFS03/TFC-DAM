# Generated by Django 4.2.1 on 2023-05-17 17:08

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Offer',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=100)),
                ('description', models.TextField()),
                ('image', models.CharField(max_length=50)),
                ('original_price', models.DecimalField(decimal_places=2, max_digits=8)),
                ('genre', models.CharField(max_length=50)),
                ('release_date', models.DateField()),
                ('developer', models.CharField(max_length=70)),
                ('publisher', models.CharField(max_length=70)),
                ('discount_percentage', models.DecimalField(decimal_places=0, max_digits=5)),
                ('start_date', models.DateField()),
                ('end_date', models.DateField()),
            ],
        ),
        migrations.CreateModel(
            name='Store',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=40)),
                ('logo', models.CharField(max_length=50)),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=50)),
                ('surnames', models.CharField(max_length=100)),
                ('email', models.EmailField(max_length=254)),
                ('password', models.CharField(max_length=150)),
                ('token', models.CharField(max_length=20, null=True, unique=True)),
                ('password_token', models.CharField(max_length=20, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='UserOffer',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('date', models.DateField(auto_now=True)),
                ('offer', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rest_api.offer')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rest_api.user')),
            ],
        ),
        migrations.AddField(
            model_name='offer',
            name='store',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='rest_api.store'),
        ),
    ]
