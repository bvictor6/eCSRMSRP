/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private Boolean isEnabled;
	
	@Column(name = "created_by", updatable = false)
	private UUID createdBy;

	@Column(name = "last_modified_by")
	private UUID lastModifiedBy;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdDate;

	@Column(name = "date_last_modified")
	private LocalDateTime lastModifiedDate;

}
