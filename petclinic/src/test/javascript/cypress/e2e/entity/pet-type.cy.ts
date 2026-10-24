import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PetType e2e test', () => {
  const petTypePageUrl = '/pet-type';
  let username: string;
  let password: string;
  const petTypeSample = { name: 'taxicab' };

  let petType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/customer/api/pet-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/customer/api/pet-types').as('postEntityRequest');
    cy.intercept('DELETE', '/services/customer/api/pet-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (petType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/customer/api/pet-types/${petType.id}`,
      }).then(() => {
        petType = undefined;
      });
    }
  });

  it('PetTypes menu should load PetTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pet-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PetType').should('exist');
    cy.location('pathname').should('eq', petTypePageUrl);
  });

  describe('PetType page', () => {
    it('should have translated page title', () => {
      cy.visit(petTypePageUrl);
      cy.getEntityHeading('PetType').should('not.contain', 'petclinicApp.customerPetType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(petTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PetType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${petTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PetType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', petTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/customer/api/pet-types',
          body: petTypeSample,
        }).then(({ body }) => {
          petType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/customer/api/pet-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/customer/api/pet-types?page=0&size=20>; rel="last",<http://localhost/services/customer/api/pet-types?page=0&size=20>; rel="first"',
              },
              body: [petType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(petTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PetType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('petType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', petTypePageUrl);
      });

      it('edit button click should load edit PetType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PetType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', petTypePageUrl);
      });

      it('edit button click should load edit PetType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PetType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', petTypePageUrl);
      });

      it('last delete button click should delete instance of PetType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('petType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', petTypePageUrl);

        petType = undefined;
      });
    });
  });

  describe('new PetType page', () => {
    beforeEach(() => {
      cy.visit(petTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PetType');
    });

    it('should create an instance of PetType', () => {
      cy.get(`[data-cy="name"]`).type('although');
      cy.get(`[data-cy="name"]`).should('have.value', 'although');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        petType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', petTypePageUrl);
    });
  });
});
