package com.micro.GateWayService.util;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidService {

    public String generateAndReturnUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}