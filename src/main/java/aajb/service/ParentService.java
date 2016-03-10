package aajb.service;

import aajb.domain.school.Parent;
import aajb.service.dto.ParentDto;
import aajb.service.exceptions.InvalidDataException;

import java.util.List;

/**
 * Created by ayed.h on 24/02/2016.
 */
public interface ParentService {
    Parent createParent(Parent parent) throws InvalidDataException;
    ParentDto createParent(ParentDto parentDto) throws InvalidDataException;
    List<ParentDto> readParents();
    List<ParentDto> readParents(int page,int size) throws InvalidDataException;
    int getNumberOfPage(int pageSize) throws InvalidDataException;
    Parent findParentByEmail(String email);
    Parent deleteParent(int id) throws InvalidDataException;
    List<ParentDto> findParents(String match);
}
