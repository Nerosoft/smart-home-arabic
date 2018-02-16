import { TestBed, inject } from '@angular/core/testing';

import { ServHttpService } from './serv-http.service';

describe('ServHttpService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ServHttpService]
    });
  });

  it('should be created', inject([ServHttpService], (service: ServHttpService) => {
    expect(service).toBeTruthy();
  }));
});
