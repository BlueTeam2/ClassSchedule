package com.softserve.repository;

import com.softserve.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends BasicRepository<Group, Long> {

    /**
     * Retrieves a group with the given id.
     *
     * @param id the id of group
     * @return an Optional describing the group with the given id or an empty Optional if none found
     */
    Optional<Group> getWithStudentsById(Long id);

    /**
     * Returns {@code true} if group with the given title exists other than the group with the given id.
     *
     * @param title the string represents the title of the group
     * @param id    the id of the group to be excluded from the search result
     * @return {@code true} if group with given title exists, otherwise {@code false}
     */
    boolean isExistsByTitleIgnoringId(String title, Long id);

    /**
     * Returns {@code true} if group with the given title exists.
     *
     * @param title the string represents the title of the group
     * @return {@code true} if group with given title exists, otherwise {@code false}
     */
    boolean isExistsByTitle(String title);

    /**
     * Returns {@code true} if group with the given id exists.
     *
     * @param id the id of the group
     * @return {@code true} if group with given id exists, otherwise {@code false}
     */
    boolean isExistsById(Long id);

    /**
     * Returns all groups with the given teacher id for default semester.
     *
     * @param id the id of the teacher
     * @return the list of groups
     */
    List<Group> getByTeacherId(Long id);

    /**
     * Returns all groups with the given Ids.
     *
     * @param groupIds the list of the ids of the groups
     * @return the list of groups
     */
    List<Group> getGroupsByGroupIds(List<Long> groupIds);

    /**
     * Returns the list of groups sorted according to the established sort order.
     *
     * @return the list of groups sorted according to the established sort order
     */
    List<Group> getAllBySortOrder();

    /**
     * Changes group's sort order.
     *
     * @param lowerBound the lower bound of sort order
     * @param upperBound the upper bound of sort order
     */
    void changeGroupOrderOffset(Integer lowerBound, Integer upperBound);

}
