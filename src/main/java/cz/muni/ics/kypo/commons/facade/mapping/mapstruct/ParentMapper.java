package cz.muni.ics.kypo.commons.facade.mapping.mapstruct;

import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import org.springframework.data.domain.Page;
/**
 * @author Roman Oravec
 */

public interface ParentMapper {

    default <T> PageResultResource.Pagination<T> createPagination(Page<?> objects) {
        PageResultResource.Pagination<T> pageMetadata = new PageResultResource.Pagination<T>();
        pageMetadata.setNumber(objects.getNumber());
        pageMetadata.setNumberOfElements(objects.getNumberOfElements());
        pageMetadata.setSize(objects.getSize());
        pageMetadata.setTotalElements(objects.getTotalElements());
        pageMetadata.setTotalPages(objects.getTotalPages());
        return pageMetadata;
    }
}
