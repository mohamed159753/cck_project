import { InstituteConsumption } from './institute-consumption';

describe('InstituteConsumption', () => {
  it('should create an object that matches the interface', () => {
    const consumption: InstituteConsumption = {
      instituteName: 'ISAMM',
      percentage: 25.5
    };
    expect(consumption).toBeTruthy();
  });
});

//This code is for the InstituteConsumption.ts interfaces