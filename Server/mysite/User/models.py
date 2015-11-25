from django.db import models

# Create your models here.

class User(models.Model):
    name = models.CharField(max_length=200, default='No Name!')
    email = models.CharField(max_length=200, default='00')
    def __unicode__(self):
        return self.name

    class Meta:
        ordering = ('name',)