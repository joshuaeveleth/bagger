GRANT USAGE ON SCHEMA ndnp TO package_modeler_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE ndnp.batch TO GROUP package_modeler_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE ndnp.batch_lccn TO GROUP package_modeler_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE ndnp.batch_reel TO GROUP package_modeler_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE ndnp.lccn TO GROUP package_modeler_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE ndnp.reel TO GROUP package_modeler_role;
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE ndnp.awardphase TO GROUP package_modeler_role;