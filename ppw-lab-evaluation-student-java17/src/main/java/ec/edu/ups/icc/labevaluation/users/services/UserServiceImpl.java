package ec.edu.ups.icc.labevaluation.users.services;
import java.util.List;
import org.springframework.stereotype.Service;
import ec.edu.ups.icc.labevaluation.users.dtos.UserResponseDto;
import ec.edu.ups.icc.labevaluation.users.mappers.UserMapper;
import ec.edu.ups.icc.labevaluation.users.repositories.UserRepository;
@Service
public class UserServiceImpl implements UserService {
    private static final int ELIGIBLE_MIN_AGE = 18;
    private final UserRepository repository;
    public UserServiceImpl(UserRepository repository){this.repository=repository;}
    @Override public List<UserResponseDto> findEligible(){
        return repository.findByAgeGreaterThanEqualAndActiveTrueAndDeletedFalseOrderByFullNameAsc(ELIGIBLE_MIN_AGE)
                .stream().map(UserMapper::toResponse).toList();
    }
}
