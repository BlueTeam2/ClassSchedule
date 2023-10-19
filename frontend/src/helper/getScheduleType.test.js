import { getScheduleType } from './getScheduleType';
import { FULL, GROUP, TEACHER } from '../constants/scheduleTypes';

describe('getScheduleType function', () => {
    it('should return type full if values is empty', () => {
        const values = {};
        expect(getScheduleType(values)).toEqual(FULL);
    });
    it('should return type group if group had id', () => {
        const group = { id: 49 };
        expect(getScheduleType({ group })).toEqual(GROUP);
    });
    it('should return type teacher if teacher had id', () => {
        const teacher = { id: 49 };
        expect(getScheduleType({ teacher })).toEqual(TEACHER);
    });
});
