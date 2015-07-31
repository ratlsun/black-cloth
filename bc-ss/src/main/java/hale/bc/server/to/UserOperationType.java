package hale.bc.server.to;

public enum UserOperationType {
	StartMock, StopMock, PauseMock, ResumeMock,
	CreateMocker, ChangeMockerOwner, ChangeMockerType, ChangeMockerName, DeleteMocker,
	WatchMocker, UnwatchMocker,
	CreateRule, UpdateRule, DeleteRule, EnableRule, DisableRule
}
