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

describe('Visit e2e test', () => {
  const visitPageUrl = '/visit';
  let username: string;
  let password: string;
  const visitSample = { description: 'ick fully', petId: 23122 };

  let visit;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/visit/api/visits+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/visit/api/visits').as('postEntityRequest');
    cy.intercept('DELETE', '/services/visit/api/visits/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (visit) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/visit/api/visits/${visit.id}`,
      }).then(() => {
        visit = undefined;
      });
    }
  });

  it('Visits menu should load Visits page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('visit');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Visit').should('exist');
    cy.location('pathname').should('eq', visitPageUrl);
  });

  describe('Visit page', () => {
    it('should have translated page title', () => {
      cy.visit(visitPageUrl);
      cy.getEntityHeading('Visit').should('not.contain', 'petclinicApp.visitVisit.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(visitPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Visit page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${visitPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Visit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', visitPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/visit/api/visits',
          body: visitSample,
        }).then(({ body }) => {
          visit = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/visit/api/visits+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/visit/api/visits?page=0&size=20>; rel="last",<http://localhost/services/visit/api/visits?page=0&size=20>; rel="first"',
              },
              body: [visit],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(visitPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Visit page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('visit');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', visitPageUrl);
      });

      it('edit button click should load edit Visit page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Visit');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', visitPageUrl);
      });

      it('edit button click should load edit Visit page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Visit');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', visitPageUrl);
      });

      it('last delete button click should delete instance of Visit', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('visit').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', visitPageUrl);

        visit = undefined;
      });
    });
  });

  describe('new Visit page', () => {
    beforeEach(() => {
      cy.visit(visitPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Visit');
    });

    it('should create an instance of Visit', () => {
      cy.get(`[data-cy="visitDate"]`).type('2025-01-30');
      cy.get(`[data-cy="visitDate"]`).blur();
      cy.get(`[data-cy="visitDate"]`).should('have.value', '2025-01-30');

      cy.get(`[data-cy="description"]`).type('jaywalk');
      cy.get(`[data-cy="description"]`).should('have.value', 'jaywalk');

      cy.get(`[data-cy="petId"]`).type('13183');
      cy.get(`[data-cy="petId"]`).should('have.value', '13183');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        visit = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', visitPageUrl);
    });
  });
});
