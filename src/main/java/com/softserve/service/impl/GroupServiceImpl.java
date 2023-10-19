package com.softserve.service.impl;

import com.softserve.entity.Group;
import com.softserve.exception.EntityNotFoundException;
import com.softserve.exception.FieldAlreadyExistsException;
import com.softserve.repository.GroupRepository;
import com.softserve.service.GroupService;
import com.softserve.service.SemesterService;
import com.softserve.repository.SortOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final SemesterService semesterService;
    private final SortOrderRepository<Group> sortOrderRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, SemesterService semesterService,
                            SortOrderRepository<Group> sortOrderRepository) {
        this.groupRepository = groupRepository;
        this.semesterService = semesterService;
        this.sortOrderRepository = sortOrderRepository;
        this.sortOrderRepository.settClass(Group.class);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Group getById(Long id) {
        log.info("In getById(id = [{}])", id);
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Group.class, "id", id.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Group getWithStudentsById(Long id) {
        log.info("In getWithStudentsById(id = [{}])", id);
        return groupRepository.getWithStudentsById(id)
                .orElseThrow(() -> new EntityNotFoundException(Group.class, "id", id.toString()));
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Group> getAll() {
        log.info("In getAll()");
        return groupRepository.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Group> getByTeacherId(Long id) {
        log.info("In service getByTeacherId(id = [{}])", id);
        return groupRepository.getByTeacherId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Group> getAllBySortOrder() {
        log.debug("Entered getAllBySortOrder() method");
        List<Group> groups = groupRepository.getAllBySortOrder();
        log.debug("Retrieved groups. Size: {}", groups.size());
        return groups;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Group createAfterOrder(Group group, Long afterId) {
        log.debug("Entered createAfterOrder");
        return sortOrderRepository.createAfterOrder(group, afterId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Group updateAfterOrder(Group group, Long afterId) {
        log.debug("Entered updateAfterOrder");
        return sortOrderRepository.updateAfterOrder(group, afterId);
    }


    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if group with input title already exists
     */
    @Transactional
    @Override
    public Group save(Group group) {
        log.info("In save(entity = [{}]", group);
        checkTitleForUniqueness(group.getTitle());
        return groupRepository.save(group);
    }

    /**
     * {@inheritDoc}
     *
     * @throws FieldAlreadyExistsException if another group with input title already exists
     */
    @Transactional
    @Override
    public Group update(Group group) {
        log.info("In update(entity = [{}]", group);
        checkTitleForUniquenessIgnoringId(group.getTitle(), group.getId());
        group.setSortOrder(sortOrderRepository.getSortOrderById(group.getId()).orElse(null));
        return groupRepository.update(group);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Group delete(Group group) {
        log.info("In delete(entity = [{}])", group);
        return groupRepository.delete(group);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isExistsById(Long id) {
        log.info("In isExistsById(id = [{}])", id);
        return groupRepository.isExistsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<Group> getDisabled() {
        log.info("Enter into getAll of getDisabled");
        return groupRepository.getDisabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Group> getGroupsBySemesterId(Long semesterId) {
        log.info("Enter into getGroupsBySemesterId");
        return semesterService.getById(semesterId).getGroups();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Group> getGroupsForCurrentSemester() {
        log.info("Enter into getGroupsByCurrentSemester");
        return semesterService.getCurrentSemester().getGroups();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Group> getGroupsForDefaultSemester() {
        log.info("Enter into getGroupsByCurrentSemester");
        return semesterService.getDefaultSemester().getGroups();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Group> getGroupsByGroupIds(List<Long> groupIds) {
        log.info("Enter into getGroupsByGroupIds");
        return groupRepository.getGroupsByGroupIds(groupIds);
    }

    private void checkTitleForUniqueness(String title) {
        if (groupRepository.isExistsByTitle(title)) {
            throw new FieldAlreadyExistsException(Group.class, "title", title);
        }
    }

    private void checkTitleForUniquenessIgnoringId(String title, Long id) {
        if (groupRepository.isExistsByTitleIgnoringId(title, id)) {
            throw new FieldAlreadyExistsException(Group.class, "title", title);
        }
    }
}
