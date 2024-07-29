/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 29, 2024
*/
package org.bcms.ecsrmsrp.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@Entity
@Table(name = "srp_country")
@Getter
@Setter
@NoArgsConstructor
public class Country {
	@Id
	private UUID id;
	private String name;
	private String nicename;
	private String iso;
	private String iso3;
	private int numcode;
	private int phonecode;

}
