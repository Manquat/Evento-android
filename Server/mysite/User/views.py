from django.shortcuts import render
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from User.models import User
from User.serializers import UserSerializer
from oauth2client import client, crypt

# Create your views here.

@api_view(['GET','POST'])
def create_user(request, format=None):
    """
    Create an User
    """
    if request.method == 'GET':
        events = User.objects.all()
        serializer = UserSerializer(events, many=True)
        return Response(serializer.data)

    elif request.method == 'POST':
        serializer = UserSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET', 'PUT', 'DELETE'])
def update_user(request, pk, format=None):
    """
    Retrieve, update or delete an User.
    """
    try:
        event = User.objects.get(pk=pk)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = UserSerializer(event)
        return Response(serializer.data)

    elif request.method == 'PUT':
        serializer = UserSerializer(event, data= request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        event.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET'])
def validate_user(request,token, format=None):
    """
    Validate User.
    """

    CLIENT_ID = "1038367220496-ceugpt9chaqucpjhhglmced2d2tat2lm.apps.googleusercontent.com"
    WEB_CLIENT_ID = "60350226207-ph9a1g0iuakvfe8bd7bbku01ktr40aur.apps.googleusercontent.com"
# (Receive token by HTTPS POST)

    try:
        idinfo = client.verify_id_token(token, CLIENT_ID)
        # If multiple clients access the backend server:
        if idinfo['aud'] not in [WEB_CLIENT_ID]:
            raise crypt.AppIdentityError("Unrecognized client.")
        if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
            raise crypt.AppIdentityError("Wrong issuer.")
    except crypt.AppIdentityError:
        return Response(status=status.HTTP_412_PRECONDITION_FAILED)
    Userid = idinfo['sub']
    try:
        user = User.objects.filter(userid__equal = Userid)
        return Response(status=status.HTTP_202_ACCEPTED)
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
