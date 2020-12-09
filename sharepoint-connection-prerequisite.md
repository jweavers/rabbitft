# Sharepoint Connection Prerequisite

To establish connection with sharepoint, we need following information, we can get them in two simple steps from Sharepoint online page.

* *Client Id* 
* *Client Secret*
* *Realm (Tenant Id)*

Assuming your sharepoint web domain is `https://YourSharePointOnlineSiteUrl/`. Then,

#### Step #1 - Register new app to your sharepoint :

Navigate to 
```
https://YourSharePointOnlineSiteUrl/_layouts/15/appregnew.aspx
``` 
![step1-sharepoint](https://user-images.githubusercontent.com/13805897/101609767-622d3000-3a2d-11eb-8fde-5739adbc9bc1.JPG)

Then, click `Generate` for `Client Id` and `Client Secret` and fill other details accordingly.

* *Title* - Name of the application.
* *App Domain* - you have any domain or otherwise localhost
* *Redirect URI* - you can specify default/home page of your sharepoint.

Once you click `Create`, you will be redirect to next page with following details :

```
Client Id:  	xxxxxxxxxxxxxxx
Client Secret:  xxxxxxxxxxxxxxxxx
Title:  	<as specified>
App Domain:  	<as specified>
Redirect URI:  	<as specified>
```

#### Step #2 - Grant permission to newly created App :

Navigate to 
```
https://YourSharePointOnlineSiteUrl/_layouts/15/appinv.aspx
```
![step2-sharepoint](https://user-images.githubusercontent.com/13805897/101609902-86890c80-3a2d-11eb-897f-f461aedb6522.JPG)

Enter your `Client Id` to `App Id` and click `Lookup` and it will retrieve all information for this application. You can also verify details, which you have provided on `Step 1`. In this page, you need to provide `Permission Request XML`. You can alway refer Sharepoint official page, to get permission XML. For now, we will grant Full control to web. So, copy following XML and paste to textbox and click `Create`.

```
<AppPermissionRequests AllowAppOnlyPolicy="true">
  <AppPermissionRequest Scope="http://sharepoint/content/sitecollection/web/list" Right="FullControl" />
  <AppPermissionRequest Scope="http://sharepoint/content/sitecollection/web" Right="FullControl" />
</AppPermissionRequests>
```

It will redirect to next page, which will ask your confirmation. Click `Trust It` and your application permision registration is done. And, you will be redirected to App collection page. On this page, you will get `App Identifier` for your app.

```
i:0i.t|ms.sp.ext|<client_id>@<bearer>
```

You can extract `Bearer` from `App Identifier`, which will be separated with `@`. And That's it !

Now, you can use above information with [rabbitFT](https://github.com/jweavers/rabbitft) to upload file programatically. 


