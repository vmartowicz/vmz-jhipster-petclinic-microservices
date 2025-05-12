import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVet } from '../vet.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vet.test-samples';

import { VetService } from './vet.service';

const requireRestSample: IVet = {
  ...sampleWithRequiredData,
};

describe('Vet Service', () => {
  let service: VetService;
  let httpMock: HttpTestingController;
  let expectedResult: IVet | IVet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VetService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Vet', () => {
      const vet = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vet', () => {
      const vet = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vet).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vet', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vet', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Vet', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVetToCollectionIfMissing', () => {
      it('should add a Vet to an empty array', () => {
        const vet: IVet = sampleWithRequiredData;
        expectedResult = service.addVetToCollectionIfMissing([], vet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vet);
      });

      it('should not add a Vet to an array that contains it', () => {
        const vet: IVet = sampleWithRequiredData;
        const vetCollection: IVet[] = [
          {
            ...vet,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVetToCollectionIfMissing(vetCollection, vet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vet to an array that doesn't contain it", () => {
        const vet: IVet = sampleWithRequiredData;
        const vetCollection: IVet[] = [sampleWithPartialData];
        expectedResult = service.addVetToCollectionIfMissing(vetCollection, vet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vet);
      });

      it('should add only unique Vet to an array', () => {
        const vetArray: IVet[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vetCollection: IVet[] = [sampleWithRequiredData];
        expectedResult = service.addVetToCollectionIfMissing(vetCollection, ...vetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vet: IVet = sampleWithRequiredData;
        const vet2: IVet = sampleWithPartialData;
        expectedResult = service.addVetToCollectionIfMissing([], vet, vet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vet);
        expect(expectedResult).toContain(vet2);
      });

      it('should accept null and undefined values', () => {
        const vet: IVet = sampleWithRequiredData;
        expectedResult = service.addVetToCollectionIfMissing([], null, vet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vet);
      });

      it('should return initial array if no Vet is added', () => {
        const vetCollection: IVet[] = [sampleWithRequiredData];
        expectedResult = service.addVetToCollectionIfMissing(vetCollection, undefined, null);
        expect(expectedResult).toEqual(vetCollection);
      });
    });

    describe('compareVet', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVet(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31928 };
        const entity2 = null;

        const compareResult1 = service.compareVet(entity1, entity2);
        const compareResult2 = service.compareVet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31928 };
        const entity2 = { id: 5685 };

        const compareResult1 = service.compareVet(entity1, entity2);
        const compareResult2 = service.compareVet(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31928 };
        const entity2 = { id: 31928 };

        const compareResult1 = service.compareVet(entity1, entity2);
        const compareResult2 = service.compareVet(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
