<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/data">
		<data>
			<result><xsl:value-of select="showSeats/@result" /></result>
			<message><xsl:value-of select="showSeats/@message" /></message>
			<xsl:variable name="GroupCode" select="showSeats/section/@id"></xsl:variable>
			<showSeats>
				<xsl:for-each select="showSeats/section[1]/seat">
					<ShowSeat>
						<code>
							<xsl:value-of select="@rowId" />_<xsl:value-of select="@columnId" />
						</code>
						<rowNum>
							<xsl:value-of select="@rowId" />
						</rowNum>
						<colNum>
							<xsl:value-of select="@columnId" />
						</colNum>
					</ShowSeat>
				</xsl:for-each>
			</showSeats>
		</data>
	</xsl:template>
</xsl:stylesheet>