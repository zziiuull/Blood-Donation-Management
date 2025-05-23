package br.ifsp.demo.domain.repository.collectionSite;

import br.ifsp.demo.domain.model.donation.CollectionSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CollectionSiteRepository extends JpaRepository<CollectionSite, UUID> {
}
