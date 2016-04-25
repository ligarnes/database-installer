package net.alteiar.db.installer.utils;

import net.alteiar.db.installer.SqlScript;

public class SimpleSqlScript implements SqlScript {

	private String name;
	private String description;
	private String query;

	@Override
	public String getName() {

		return name;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public String getQuery() {

		return query;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
