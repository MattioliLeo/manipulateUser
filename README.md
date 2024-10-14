# Manipulate user
This project is about impersonation a user via Keycloak REST API.

Yet "impersonation" is not the correct term, you should talk about token-exchange.

## Keycloak conf
Token exchange is an experimental feature in Keycloak, so you have to enable features preview.
```
bin/kc.sh start-dev --features="preview"
```
this will enable all the experimental feature; there's probably a way to cherry-pick only the token-exchange related.

### Client configuration
- create the private Client [image1](docs/img/001-create-client-01.png) [image2](docs/img/001-create-client-02.png) [image3](docs/img/001-create-client-03.png)
- write down client credential [image](docs/img/002-client-credentials.png)
- enable client permission [image](docs/img/003-client-permissions.png)
- create a new permission for token-exchange [image](docs/img/004-Permission%20Details.png)
- go to client "Realm-management" -> Authorization -> Policies [image](docs/img/005-realm-policy.png)
- create a new Policy, type "Client" [image](docs/img/006-realm-policy-create.png)
- give it a name and link the new policy to private client created [image](docs/img/007-realm-policy-create-detail.png)
- now create a Scope-based permission [image](docs/img/008-realm-permission-create.png)
- select the correct data [image](docs/img/009-realm-permission-create-details.png) 
  - Resources=Users
  - Auth scope = impersonate
  - Policies = the one created

### Admin user
Now you need to create or select a user that will be used to impersonate all the other users and request the tokens
In my example I create a new one called svc-impersonate
- Add a user [image](docs/img/010-Add%20user.png)
- put the user details [image](docs/img/011-New%20user.png)
- set the credentials [image](docs/img/012-credentials.png) and confirm
- go to Role Mapping [image](docs/img/013-user%20role%20mapping.png)
- select the mappings as in [image](docs/img/016-mappingsToSet.png). When Assigning roles pay attention that they are filtered, choose the correct filter to search for the one needed


