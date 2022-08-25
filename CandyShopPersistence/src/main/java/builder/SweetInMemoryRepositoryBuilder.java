package builder;

import domain.sweet.Sweet;
import exception.ValidationException;
import lombok.NoArgsConstructor;
import repository.sweetRepository.SweetInMemoryRepository;

import java.util.List;

@NoArgsConstructor
public class SweetInMemoryRepositoryBuilder {
    public SweetInMemoryRepository build(List<Sweet> sweetList) throws ValidationException {
        return SweetInMemoryRepository.builder()
                .sweetList(sweetList)
                .build();
    }
}
