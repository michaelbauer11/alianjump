from django.conf.urls import url, include

urlpatterns = [ 
    url(r'^api/', include('forms.urls')), 
    url(r'^api/', include('management.urls')),
    url(r'^api/',include("users.urls")),
    url(r'^api/',include("msgs.urls")),
]
