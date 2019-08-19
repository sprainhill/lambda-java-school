package com.lambdaschool.school.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// when we an http status not founbd, we're gonna do these things
@ResponseStatus(value = HttpStatus.NOT_FOUND)
// Were gonna take some methods off of RuntimeException to do these things
public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
}
