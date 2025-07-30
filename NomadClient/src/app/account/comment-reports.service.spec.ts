import { TestBed } from '@angular/core/testing';

import { CommentReportsService } from './comment-reports.service';

describe('CommentReportsService', () => {
  let service: CommentReportsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommentReportsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
