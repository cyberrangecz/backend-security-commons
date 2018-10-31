package cz.muni.ics.kypo.commons.facade.mapping;

import cz.muni.ics.kypo.commons.facade.api.PageResultResource;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BeanMapping {
	public <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

	public <T> Page<T> mapTo(Page<?> objects, Class<T> mapToClass);

	public <T> PageResultResource<T> mapToPageResultDTO(Page<?> objects, Class<T> mapToClass);

	public <T> Set<T> mapToSet(Collection<?> objects, Class<T> mapToClass);

	public <T> Optional<T> mapToOptional(Object u, Class<T> mapToClass);

	public <T> T mapTo(Object u, Class<T> mapToClass);
}
