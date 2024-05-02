package ie.setu.volunteerhub.models


interface DonationStore {
    fun findAll() : List<DonationModel>
    fun findById(id: Long) : DonationModel?
    fun create(donation: DonationModel)
}