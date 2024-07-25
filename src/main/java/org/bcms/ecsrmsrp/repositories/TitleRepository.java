/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.repositories;

import java.util.UUID;

import org.bcms.ecsrmsrp.entities.Title;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface TitleRepository extends JpaRepository<Title, UUID>{

}
