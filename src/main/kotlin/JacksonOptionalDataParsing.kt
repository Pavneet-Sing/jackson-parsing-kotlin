import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.registerKotlinModule


// Demonstrate the
// - Use of Jackson with Kotlin
// - Use of optional/missing values in data

class JacksonOptionalDataParsing {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            parseXML()
            parseJson()

        }

        private fun parseXML() {
            //======================= Handle Empty Tags=====================
            val dataEmptySpecialStr = """
                        <TABLES>
                            <TABLE NAME="abcd" TIME="2013.05.27 00:00:00" >
                                <SPECIAL></SPECIAL> 
                            </TABLE>
                        </TABLES>
                    """.trimIndent()
            val xmlMapper = XmlMapper()

            println(xmlMapper.readValue(dataEmptySpecialStr, TABLES::class.java))

            //======================= Handle Missing Values=====================
            val dataStr = """
                        <TABLES>
                            <TABLE NAME="abcd" TIME="2013.05.27 00:00:00" >
                                <SPECIAL>
                                    <WEEK NAME="abcde" PARAM="128" />
                                </SPECIAL> 
                            </TABLE>
                        </TABLES>
                    """.trimIndent()

            println(xmlMapper.readValue(dataStr, TABLES::class.java))
            // Output
            // TABLES(table=TABLE(special=null, name=abcd, time=2013.05.27 00:00:00))
        }

        private fun parseJson() {
            val dataJSONStr = """
                {
                    "name": "Pavneet"
                }
            """.trimIndent()

//            val mapper = ObjectMapper()
            // will use kotlin.jvm reflection classes for KClass to Java class mapping, same for KFunction
            val mapper = ObjectMapper().registerKotlinModule()
            println(mapper.readValue(dataJSONStr, Person::class.java))

        }
    }
}
// ============================ JSON Specific Data Classes ===================

// won't have a default constructor
data class Person(var name: String, var occupation: String?)

// Will have default constructor
/*
class Person{
    lateinit var name: String
    var occupation: String? = null
}
*/

/*
data class Person(
    @JsonProperty("name") val name: String,
    @JsonProperty("occupation") val occupation: String?
)
*/

// ============================ XML Specific Data Classes ===================
data class TABLES(@JacksonXmlProperty(localName = "TABLE") val table: TABLE)

data class TABLE(
    @JacksonXmlProperty(localName = "SPECIAL") val special: SPECIAL?,
    @JacksonXmlProperty(localName = "NAME") val name: String,
    @JacksonXmlProperty(localName = "TIME") val time: String
)

data class SPECIAL(
    @JacksonXmlProperty(localName = "WEEK") var week: WEEK?,
    @JacksonXmlProperty(localName = "DAY") var day: DAY?
)

data class DAY(
    @JacksonXmlProperty(localName = "DATE") val date: String,
    @JacksonXmlProperty(localName = "MASK") val mask: String
)

data class WEEK(
    @JacksonXmlProperty(localName = "PARAM") val param: String,
    @JacksonXmlProperty(localName = "NAME") val name: String
)