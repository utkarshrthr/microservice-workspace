package com.client.config.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.client.config.app.config.DbSettings;

@RestController
@RefreshScope
public class ClientConfigController {

	@Autowired
	private DbSettings settings;
	
	@GetMapping("/dbconfig")
	public String getDbConfig() {
		return settings.getName() + ", " + settings.getPort();
	}
}
