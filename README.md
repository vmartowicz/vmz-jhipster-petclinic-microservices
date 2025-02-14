# Jhipster Petclinic Microservices

## Development

### Start yours dependencies services:

#### To start the registry, run :
```
sudo docker compose -f petclinic/src/main/docker/consul.yml up -d
```
#### To start yours application microservices, run:
```
cd customer && ./mvnw
cd vet && ./mvnw
cd visit && ./mvnw
cd petclinic && ./mvnw 
```

## Production

### Start yours dependencies services:

#### To start the registry, run :
```
sudo docker compose -f petclinic/src/main/docker/consul.yml up -d
```

#### To start your databases microservices , run :
```
sudo docker compose -f petclinic/src/main/docker/postgresql.yml up -d
sudo docker compose -f customer/src/main/docker/postgresql.yml up -d
sudo docker compose -f vet/src/main/docker/postgresql.yml up -d
sudo docker compose -f visit/src/main/docker/postgresql.yml up -d
```

#### To start yours application microservices, run:
```
cd customer && ./mvnw
cd vet && ./mvnw
cd visit && ./mvnw
cd petclinic && ./mvnw 
```
