import { UniversityStats } from './university-stats';

describe('UniversityStats', () => {
  it('should create an object that matches the interface', () => {
    const stats: UniversityStats = {
      totalReservations: 100,
      totalInstitutes: 5,
      totalProfessors: 50
    };
    expect(stats).toBeTruthy();
  });
});
