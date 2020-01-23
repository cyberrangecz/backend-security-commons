# KYPO Security commons
This project represents the commons project for other back-end projects. This project adds security configuration and manages the roles of groups in other projects.

## Authors

Name          | Email          
------------- | ------------
Pavel Šeda    |   441048@mail.muni.cz
Dominik Pilár |   445537@mail.muni.cz
Jan Duda      |   394179@mail.muni.cz
Boris Jaduš   |   445343@mail.muni.cz

## Content

1.  Usage of This Library
2.  Setting up the Project with Imported Security Commons (Project Configuration)

## 1. Usage of This Library
First, install project with command bellow:
```
mvn install
```

Add this Maven dependency to your `pom.xml`: 
```        
<dependency>
    <groupId>cz.muni.ics.kypo</groupId>
    <artifactId>kypo2-security-commons</artifactId>
    <version>${kypo2-security-commons-actual-version}</version>
</dependency>
```
NOTE: Change version to currently released version of kypo2-security-commons.

## 2. Setting up the Project with Imported Security Commons (Project Configuration)
### Creation of JSON File with Roles 

You need to create file `roles.json` in the classpath of your project. In the file, you can define initial roles for your microservice. 
For each role, you only need to specify role type, status if the role is default or not (notice that only one role can be the default) 
and a description of the role (a description of what the user is capable of with this role). The role type format must be as follows 
ROLE{NAME_OF_MICROESERVICE}_{ROLE_TYPE} as an example below. Enum class of roles must contain the same values as defined role types in the JSON file.
 
```json
[
  {
    "role_type": "ROLE_TRAINING_ADMINISTRATOR",
    "default": false,
    "description": "This role will allow you to ..."
  }
]
```

### OpenID Connect Credentials and Preparation of Property File
After the previous steps you have to add to the properties file of project which import security-commons all fields as follows:
* Create a .properties file according to the format shown in the [kypo2 security commons example file](kypo2-security-commons-example.properties). Set the required attributes and save the file.

The tutorials for setting up:
* [HTTPS and CA](https://gitlab.ics.muni.cz/kypo2/kypo2-project/wikis/Creation-of-Self-Signed-Certificate-and-Import-It-to-CA),
* [OpenID Connect configuration settings](https://gitlab.ics.muni.cz/kypo2/kypo2-project/wikis/Setting-up-a-Relying-Party-and-Resource-Server-in-OIDC-Provider),

are available at the [KYPO2 project wiki](https://gitlab.ics.muni.cz/kypo2/kypo2-project/wikis/home).
