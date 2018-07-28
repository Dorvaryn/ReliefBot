// BallPrediction.cpp : Defines the exported functions for the DLL application.
//

#include "flat/rlbot_generated.h"
#include "PredictionService.hpp"
#include "BallPrediction.h"


rlbot::flat::Vector3 convertVec(vec3 vec)
{
	return rlbot::flat::Vector3(vec[0], vec[1], vec[2]);
}

vec3 convertVec(const rlbot::flat::Vector3* vec)
{
	return vec3{ vec->x(), vec->y(), vec->z() };
}

static PredictionService predictionService(5.0f, 0.2f);

bool FillBallPrediction(BallSlice ballSlice, flatbuffers::FlatBufferBuilder* builder)
{

	std::list<BallSlice>* prediction = predictionService.updatePrediction(ballSlice);

	std::vector<flatbuffers::Offset<rlbot::flat::PredictionSlice>> slices;

	for (std::list<BallSlice>::iterator it = prediction->begin(); it != prediction->end(); it++) {

		rlbot::flat::PhysicsBuilder physicsBuilder(*builder);
		physicsBuilder.add_location(&convertVec(it->Location));
		physicsBuilder.add_velocity(&convertVec(it->Velocity));
		physicsBuilder.add_angularVelocity(&convertVec(it->AngularVelocity));

		auto physOffset = physicsBuilder.Finish();

		rlbot::flat::PredictionSliceBuilder sliceBuilder(*builder);
		sliceBuilder.add_gameSeconds(it->gameSeconds);
		sliceBuilder.add_physics(physOffset);
		slices.push_back(sliceBuilder.Finish());
	}

	auto slicesOffset = builder->CreateVector(slices);

	rlbot::flat::BallPredictionBuilder predictionBuilder(*builder);
	predictionBuilder.add_slices(slicesOffset);

	builder->Finish(predictionBuilder.Finish());

	return true;

}

void FetchBallPrediction(void* ballSliceFlatbuffer, int size, flatbuffers::FlatBufferBuilder* predictionBuilder)
{
	auto flatSlice = flatbuffers::GetRoot<rlbot::flat::PredictionSlice>(ballSliceFlatbuffer);
	BallSlice slice;
	slice.Location = convertVec(flatSlice->physics()->location());
	slice.Velocity = convertVec(flatSlice->physics()->velocity());
	slice.AngularVelocity = convertVec(flatSlice->physics()->angularVelocity());
	slice.gameSeconds = flatSlice->gameSeconds();

	FillBallPrediction(slice, predictionBuilder);
}

