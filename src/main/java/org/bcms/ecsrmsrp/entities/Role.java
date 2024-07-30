/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@Table(name = "srp_role")
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {
	
	private String name;
	private String description;

	/*@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "role", orphanRemoval = true)
	private List<User> users;*/
}
