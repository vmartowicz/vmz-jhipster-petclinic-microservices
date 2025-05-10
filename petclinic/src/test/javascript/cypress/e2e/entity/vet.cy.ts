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

describe('Vet e2e test', () => {
  const vetPageUrl = '/vet';
  const vetPageUrlPattern = new RegExp('/vet(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vetSample = { firstName: 'Abdul', lastName: 'Murray' };

  let vet;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/vet/api/vets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/vet/api/vets').as('postEntityRequest');
    cy.intercept('DELETE', '/services/vet/api/vets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/vet/api/vets/${vet.id}`,
      }).then(() => {
        vet = undefined;
      });
    }
  });

  it('Vets menu should load Vets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Vet').should('exist');
    cy.url().should('match', vetPageUrlPattern);
  });

  describe('Vet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Vet page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vet/new$'));
        cy.getEntityCreateUpdateHeading('Vet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/vet/api/vets',
          body: vetSample,
        }).then(({ body }) => {
          vet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/vet/api/vets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/vet/api/vets?page=0&size=20>; rel="last",<http://localhost/services/vet/api/vets?page=0&size=20>; rel="first"',
              },
              body: [vet],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Vet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vet');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vetPageUrlPattern);
      });

      it('edit button click should load edit Vet page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vetPageUrlPattern);
      });

      it('edit button click should load edit Vet page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Vet');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vetPageUrlPattern);
      });

      it('last delete button click should delete instance of Vet', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('vet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vetPageUrlPattern);

        vet = undefined;
      });
    });
  });

  describe('new Vet page', () => {
    beforeEach(() => {
      cy.visit(`${vetPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Vet');
    });

    it('should create an instance of Vet', () => {
      cy.get(`[data-cy="firstName"]`).type('Jocelyn');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Jocelyn');

      cy.get(`[data-cy="lastName"]`).type('Volkman');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Volkman');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        vet = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vetPageUrlPattern);
    });
  });
});
