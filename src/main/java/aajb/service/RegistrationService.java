package aajb.service;

import aajb.service.dto.RegistrationDto;
import aajb.service.exceptions.InvalidDataException;

import java.text.ParseException;

/**
 * Created by ayed.h on 01/03/2016.
 */
public interface RegistrationService {
    RegistrationDto createRegistration(RegistrationDto registrationDto) throws InvalidDataException;
    void deleteRegistration(int id) throws InvalidDataException;
}
