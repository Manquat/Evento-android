# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
import datetime
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('events', '0008_auto_20151023_1537'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='date',
            field=models.DateTimeField(default=datetime.datetime(2015, 10, 23, 13, 44, 55, 334000, tzinfo=utc), verbose_name=b'date'),
        ),
    ]