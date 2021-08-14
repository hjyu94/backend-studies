package me.hjeong.catalogservice.service;

import me.hjeong.catalogservice.entity.CatalogEntity;

public interface CatalogService {

    Iterable<CatalogEntity> getAllCatalogs();

}
