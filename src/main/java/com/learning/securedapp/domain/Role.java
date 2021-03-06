package com.learning.securedapp.domain;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
/**
 * <p>Role class.</p>
 *
 * @author rajakolli
 * @version $Id: $Id
 */
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

	private static final long serialVersionUID = -6260569351005920639L;

	@Id
	private String id;

	@Indexed
	@NotNull
	private String roleName;
	private String description;
	@DBRef(lazy = true)
	private List<Permission> permissions;
}
