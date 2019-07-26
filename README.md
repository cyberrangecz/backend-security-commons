# KYPO2 Security commons
This project represents commons project for other back-end projects. This project adds security configuration and manages roles of groups in others projects.

## Using This Project
First install project with command bellow:
```
mvn install
```

Add this Maven dependency to your POM in persistence module: 
```        
<dependency>
    <groupId>cz.muni.ics.kypo</groupId>
    <artifactId>kypo2-security-commons</artifactId>
    <version>${kypo2-security-commons-actual-version}</version>
</dependency>
```
NOTE: Change version to currently released version of kypo2-security-commons.

## How to set up the project with imported security commons
### 1. Creating JSON file with roles 

You need to create file roles.json in classpath of your project. In file you can define initial roles for your microservice. 
For each role, you only need to specify role type, status if role is default or not (notice that only one role can be default) 
and description of role (a description of what the user is capable of with this role) . The role type format must be as follows 
ROLE_{name of microservice}_{role type} as example below. Enum class of roles must contain same values as defined role types in JSON file.
 
```json
[
  {
    "role_type": "ROLE_TRAINING_ADMINISTRATOR",
    "default": false,
    "description": "This role will allow you ..."
  }
]
```

### 2. Getting Masaryk University OpenID Connect credentials 

1. Go to `https://oidc.ics.muni.cz/oidc/` and log in.
2. Click on "**Self-service Client Registration**" -> "**New Client**".
3. Set Client name.
4. Add at least one custom Redirect URI and `http://localhost:8080/{context path from external properties file}/webjars/springfox-swagger-ui/oauth2-redirect.html` (IMPORTANT for Swagger UI).
5. In tab "**Access**":
    1. choose which information about user you will be getting, so called `scopes`.
    2. select just *implicit* in **Grand Types**
    3. select *token* and *code id_toke* in **Responses Types**
6. Hit **Save** button.
7. Then got to tab "**JSON**", copy the JSON file and save it to file. **IMPORTANT STEP**
8. Now create new Resource in "**Self-service Protected Resource Registration**".
9. Again insert client Name and save JSON to external file in "**JSON**" tab.
10. In tab "**Access**" again choose which information about user you will be getting, so called `scopes`.
11. Hit **Save** button.


### 3. Properties file

After the previous steps you have to add to the properties file of project which import security-commons all fields shown in [kypo2 security commons example file](kypo2-security-commons-example.properties) and save it. 
