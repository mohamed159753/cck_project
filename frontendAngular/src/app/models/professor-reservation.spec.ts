import { ProfessorReservation } from './professor-reservation';

describe('ProfessorReservation', () => {
  it('should create an object that matches the interface', () => {
    const reservation: ProfessorReservation = {
      name: 'Prof.Karim Lotfi',
      institute: 'ISAMM',
      numberOfReservations: 5,
      lastUsed: '2025-04-15'
    };
    expect(reservation).toBeTruthy();
  });
});
