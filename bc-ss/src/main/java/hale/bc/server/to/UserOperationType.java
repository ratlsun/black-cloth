package hale.bc.server.to;

public enum UserOperationType {
	StartMock, StopMock, PauseMock, ResumeMock,
	CreateMocker, ChangeMockerOwner, ChangeMockerType, ChangeMockerName, DeleteMocker, CollectMocker, CancelCollectMocker,
	CreateRule, UpdateRule, DeleteRule, EnableRule, DisableRule
}
