# CyberRangeCZ Security commons

This project represents the commons project for other back-end projects. This project adds security
configuration and manages the roles of groups in other projects.

## Content

1. Usage of This Library
2. Setting up the Project with Imported Security Commons (Project Configuration)

## 1. Usage of This Library

First, install the project with the command bellow:

```
mvn install
```

Add this Maven dependency to your `pom.xml`:

```
<dependency>
    <groupId>cz.cybersecurityhub.maven</groupId>
    <artifactId>security-commons</artifactId>
    <version>${security-commons-version}</version>
</dependency>
```

**Requirements:**

- Java 21 or higher
- Spring Boot 3.x (compatible with Spring Boot 3.5.3)

### Register microservice

To automatically register microservice in the user-and-group service add the following import
to your configuration file:

```
@Import(value = MicroserviceRegistrationConfiguration.class)
```

Then you need to create the file `roles.json` in the classpath of your project. In the file, you can
define initial roles for your microservice.
For each role, you only need to specify role type, status if the role is default or not (notice that
only one role can be the default)
and a description of the role (a description of what the user is capable of with this role). The
role type format must be as follows
ROLE{NAME_OF_MICROESERVICE}_{ROLE_TYPE} as an example below. Enum class of roles must contain the
same values as defined role types in the JSON file.

```json
[
  {
    "role_type": "ROLE_TRAINING_ADMINISTRATOR",
    "default": false,
    "description": "This role will allow you to ..."
  }
]
```

### OAuth2 security

To add OAuth2 security configuration add the following import to your configuration file:

```
@Import(value = ResourceServerSecurityConfig.class)
```

## 2. Setting up the Project with Imported Security Commons (Project Configuration)

After the previous steps, you have to add to the properties file of the project which imports
security-commons library all fields according to the format shown in
the [crczp security commons example file](https://github.com/cyberrangecz/backend-security-commons/blob/master/security-commons-example.properties).
Some fields require to set
up [OpenID Connect configuration](https://docs.platform.cyberrange.cz/installation-guide/setting-up-oidc-provider/)
and running [user-and-group](https://github.com/cyberrangecz/backend-user-and-group) microservice.
