import React from 'react';
import { shallow } from 'enzyme';
import LessonsCard from './LessonsCard';

const onClickOpen = jest.fn();
const onSelectLesson = jest.fn();
const onCopyLesson = jest.fn();

const props = {
    lesson: {
        id: 1,
        group: { id: 91, title: '11(А)' },
        grouped: true,
        hours: 2,
        lessonType: 'LABORATORY',
        linkToMeeting: 'http://localhost:3000/admin/lessons',
        subjectForSite: 'Web-дизайн',
        teacher: {
            id: 1,
            name: 'n.',
            surname: 'surname',
            patronymic: 'p.',
            position: 'position',
        },
    },
    onClickOpen,
    onSelectLesson,
    onCopyLesson,
};

describe('LessonsCard component if grouped', () => {
    let wrapper;
    beforeEach(() => {
        wrapper = shallow(<LessonsCard {...props} grouped />);
    });

    it('should render FaUserPlus if grouped', () => {
        expect(wrapper.find('FaEdit')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
        expect(wrapper.find('MdContentCopy')).toHaveLength(1);
        expect(wrapper.find('FaUserPlus')).toHaveLength(1);
    });
});

describe('LessonsCard component if no grouped', () => {
    let wrapper;
    beforeEach(() => {
        wrapper = shallow(<LessonsCard {...props} grouped={false} />);
    });

    it('should render FaEdit,MdDelete,MdContentCopy if no grouped', () => {
        expect(wrapper.find('FaEdit')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
        expect(wrapper.find('MdContentCopy')).toHaveLength(1);
    });

    it('should call onClickOpen when click to MdDelete icon delete', () => {
        wrapper.find('MdDelete').simulate('click');
        expect(props.onClickOpen).toHaveBeenCalled();
        expect(props.onClickOpen).toHaveBeenCalledWith(props.lesson.id);
    });

    it('should call onSelectLesson when click to FaEdit icon delete', () => {
        wrapper.find('FaEdit').simulate('click');
        expect(props.onSelectLesson).toHaveBeenCalled();
        expect(props.onSelectLesson).toHaveBeenCalledWith(props.lesson.id);
    });

    it('should call onCopyLesson when click to MdContentCopy icon delete', () => {
        wrapper.find('MdContentCopy').simulate('click');
        expect(props.onCopyLesson).toHaveBeenCalled();
        expect(props.onCopyLesson).toHaveBeenCalledWith(props.lesson);
    });

    it('should render lesson title', () => {
        const title = wrapper.find('.lesson-card__title').text();

        expect(title).toEqual(props.lesson.subjectForSite);
    });

    it('should render lesson teacherName surname n. p.', () => {
        const teacherName = wrapper.find('.lesson-card__teacher').text();
        const { surname, name, patronymic } = props.lesson.teacher;
        expect(teacherName).toEqual(`${surname} ${name} ${patronymic}`);
    });

    it('should render linkToMeeting if link passed', () => {
        const link = wrapper.find('.lesson-card__link').text();

        expect(link).toEqual(props.lesson.linkToMeeting);
    });
});
