package cz.muni.ics.kypo.commons.facade.mapping.mapstruct;

import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import cz.muni.ics.kypo.commons.facade.api.dto.IDMGroupRefDTO;
import cz.muni.ics.kypo.commons.persistence.model.IDMGroupRef;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

/**
 * @author Roman Oravec
 */
@Mapper(componentModel = "spring")
public interface IDMGroupRefMapper extends ParentMapper{

    IDMGroupRef mapToGroup(IDMGroupRefDTO idmGroupRefDTO);

    IDMGroupRefDTO mapToGroupDTO(IDMGroupRef idmGroupRef);

    List<IDMGroupRef> mapToGroupList(Collection<IDMGroupRefDTO> idmGroupRefDTOS);

    List<IDMGroupRefDTO> mapToGroupDTOList(Collection<IDMGroupRef> idmGroupRefs);

    Set<IDMGroupRef> mapToGroupSet(Collection<IDMGroupRefDTO> idmGroupRefDTOS);

    Set<IDMGroupRefDTO> mapToGroupDTOSet(Collection<IDMGroupRef> idmGroupRefs);

    default Optional<IDMGroupRef> mapToGroupOptional(IDMGroupRefDTO idmGroupRefDTO) {
        return Optional.ofNullable(mapToGroup(idmGroupRefDTO));
    }

    default Optional<IDMGroupRefDTO> mapToGroupDTOOptional(IDMGroupRef idmGroupRef){
        return Optional.ofNullable(mapToGroupDTO(idmGroupRef));
    }

    default Page<IDMGroupRef> mapToGroupPage(Page<IDMGroupRefDTO> groupsRefDTOS){
        List<IDMGroupRef> mappedGroups = mapToGroupList(groupsRefDTOS.getContent());
        return new PageImpl<>(mappedGroups, groupsRefDTOS.getPageable(), mappedGroups.size());
    }

    default Page<IDMGroupRefDTO> mapToGroupDTOPage(Page<IDMGroupRef> idmGroupRefs){
        List<IDMGroupRefDTO> mappedGroups = mapToGroupDTOList(idmGroupRefs.getContent());
        return new PageImpl<>(mappedGroups, idmGroupRefs.getPageable(), mappedGroups.size());
    }

    default PageResultResource<IDMGroupRefDTO> mapToPageResultGroupDTO(Page<IDMGroupRef> idmGroupRefs){
        List<IDMGroupRefDTO> mappedGroups = new ArrayList<>();
        idmGroupRefs.forEach(group -> mappedGroups.add(mapToGroupDTO(group)));
        return new PageResultResource<>(mappedGroups, createPagination(idmGroupRefs));
    }
}
