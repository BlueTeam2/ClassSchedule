import { shallow } from 'enzyme';
import { setLink } from './setLInk';
import { places } from '../constants/places';

const card = { link: 'https://www.google.com/' };

describe('setLink function', () => {
    it('should return LinkToMeeting component if places.TOGETHER', () => {
        const wrapper = shallow(setLink(card, places.TOGETHER));
        expect(wrapper.exists()).toBeTruthy();
    });
    it('should return link with href if places.ONLINE', () => {
        const wrapper = shallow(setLink(card, places.ONLINE));
        expect(wrapper.exists()).toBeTruthy();
    });
    it('should return null if places = null', () => {
        expect(setLink(card)).toBeNull();
    });
});
