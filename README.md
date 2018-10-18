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
    <artifactId>kypo2-security-commons-rest</artifactId>
    <version>${kypo2-security-commons-actual-version}</version>
</dependency>
```

add plugin: 
```
<plugin>
				<groupId>org.flywaydb</groupId>
				<artifactId>flyway-maven-plugin</artifactId>
				<configuration>
					<sqlMigrationSeparator>__</sqlMigrationSeparator>
					<locations>
						<location>classpath:db.migration</location>
					</locations>
					<url>${jdbc.url}</url>
					<user>${jdbc.username}</user>
					<password>${jdbc.password}</password>
				</configuration>
				<dependencies>
					<dependency>
						<groupId>org.postgresql</groupId>
						<artifactId>postgresql</artifactId>
						<version>${postgresql.version}</version>
					</dependency>
				</dependencies>
			</plugin>
```
NOTE: Change version to currently released version of kypo2-rest-commons.



Use  "**@Import({WebConfigRestSecurityCommons.class})**" in your service layer config class

For using roles in your project, you have to add **roles.properties** file to your resource 
package in your service layer. For each role you only need to specify role type which will be used in your project. 
Roles are stored as string in DB but they are rewritten to upperCase. So roles in example below are stored as GUEST, ADMINISTRATOR and
USER.  
 
```properties
kypo.commons.roles=Guest, administrator, USER
```

## How to set up the project with imported security commons

### 1. Getting Masaryk University OpenID Connect credentials 

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


### 2. Properties file

After step 1 you have to add this to your properties file according to format below and save it.
```properties
# Environment DEV or PROD
spring.profiles.active=DEV # DEV environment does not need kypo2-user-and-group project but assign authority as GUEST by default


# OpenID Connect
kypo.idp.4oauth.introspectionURI=https://oidc.ics.muni.cz/oidc/introspect
kypo.idp.4oauth.authorizationURI=https://oidc.ics.muni.cz/oidc/authorize
kypo.idp.4oauth.resource.clientId={your client ID from Self-service protected resource}
kypo.idp.4oauth.resource.clientSecret={your client secret from Self-service protected resource}
kypo.idp.4oauth.client.clientId={your client ID from Self-service client}
kypo.idp.4oauth.scopes=openid, email
# you can add more scopes according to settings from step 1.

server.url={your server url}
#for exmample
server.url=https://localhost:8080/
```
